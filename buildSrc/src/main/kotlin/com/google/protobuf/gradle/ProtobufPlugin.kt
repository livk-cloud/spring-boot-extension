package com.google.protobuf.gradle

import com.google.gradle.osdetector.OsDetectorPlugin
import com.google.protobuf.gradle.tasks.ProtoSourceSet
import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import org.gradle.api.file.FileCollection
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.plugins.AppliedPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.language.jvm.tasks.ProcessResources
import java.io.File
import java.util.function.Supplier

/**
 * @author livk
 */
class ProtobufPlugin : Plugin<Project> {

	companion object {
		val PREREQ_PLUGIN_OPTIONS = listOf("java", "java-library")
	}

	private lateinit var project: Project
	private lateinit var protobufExtension: ProtobufExtension
	private var wasApplied = false

	override fun apply(project: Project) {
		this.protobufExtension = project.extensions.create("protobuf", ProtobufExtension::class.java, project)
		this.project = project

		project.pluginManager.apply(OsDetectorPlugin::class.java)


		val applyWithPrerequisitePlugin: Action<in AppliedPlugin> = Action {
			if (wasApplied) {
				project.logger.info("The com.google.protobuf plugin was already applied to the project: ' + project.path and will not be applied again after plugin:${it.id}")
			} else {
				wasApplied = true

				doApply()
			}
		}

		PREREQ_PLUGIN_OPTIONS.forEach { project.pluginManager.withPlugin(it, applyWithPrerequisitePlugin) }

		project.afterEvaluate {
			if (!wasApplied) {
				throw GradleException("The com.google.protobuf plugin could not be applied during project evaluation. The Java plugin or one of the Android plugins must be applied to the project first.")
			}
		}
	}

	private fun doApply() {
		val postConfigure = mutableListOf<Supplier<*>>()

		project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.all {
			val protoSourceSet = protobufExtension.sourceSets.create(it.name)
			addSourceSetExtension(it, protoSourceSet)
			val protobufConfig = createProtobufConfiguration(protoSourceSet)
			val compileProtoPath = createCompileProtoPathConfiguration(protoSourceSet)
			addTasksForSourceSet(it, protoSourceSet, protobufConfig, compileProtoPath, postConfigure)
		}

		project.afterEvaluate {
			this.protobufExtension.configureTasks()
			// Disallow user configuration outside the config closures, because the operations just
			// after the doneConfig() loop over the generated outputs and will be out-of-date if
			// plugin output is added after this point.
			this.protobufExtension.generateProtoTasks.all().configureEach { it.doneConfig() }
			postConfigure.forEach { it.get() }
			// protoc and codegen plugin configuration may change through the protobuf{}
			// block. Only at this point the configuration has been finalized.
			protobufExtension.tools.resolve(project)
		}
	}

	private fun createProtobufConfiguration(protoSourceSet: ProtoSourceSet): Configuration {
		val protobufConfigName = Utils.getConfigName(protoSourceSet.name, "protobuf")
		return project.configurations.create(protobufConfigName) {
			it.isVisible = false
			it.isTransitive = true
		}
	}

	private fun createCompileProtoPathConfiguration(protoSourceSet: ProtoSourceSet): Configuration {
		val compileProtoConfigName = Utils.getConfigName(protoSourceSet.name, "compileProtoPath")
		val compileClasspathConfig = project.configurations.getByName(
			Utils.getConfigName(
				protoSourceSet.name,
				JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME
			)
		)
		return project.configurations.create(compileProtoConfigName) { compileProto ->
			compileProto.isVisible = false
			compileProto.isTransitive = true
			compileProto.extendsFrom(compileClasspathConfig)
			compileProto.isCanBeConsumed = false
			compileProto.attributes { attributes ->
				attributes.attribute(
					LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE,
					project.objects.named(LibraryElements::class.java, LibraryElements.RESOURCES)
				).attribute(
					Usage.USAGE_ATTRIBUTE,
					project.objects.named(Usage::class.java, Usage.JAVA_RUNTIME)
				)
			}
		}
	}

	private fun addSourceSetExtension(sourceSet: SourceSet, protoSourceSet: ProtoSourceSet): SourceDirectorySet {
		val name = sourceSet.name
		val sds = protoSourceSet.proto
		sourceSet.extensions.add("proto", sds)
		sds.srcDir("src/${name}/proto")
		sds.include("**/*.proto")
		return sds
	}

	private fun addTasksForSourceSet(
		sourceSet: SourceSet, protoSourceSet: ProtoSourceSet, protobufConfig: Configuration,
		compileProtoPath: Configuration, postConfigure: MutableList<Supplier<*>>
	) {
		val extractProtosTask = setupExtractProtosTask(protoSourceSet, protobufConfig)
		val extractIncludeProtosTask = setupExtractIncludeProtosTask(protoSourceSet, compileProtoPath)

		if (Utils.isTest(sourceSet.name)) {
			protoSourceSet.includesFrom(protobufExtension.sourceSets.getByName("main"))
		}

		val generateProtoTask = addGenerateProtoTask(protoSourceSet) {
			it.sourceSet = sourceSet
			it.doneInitializing()
			it.builtins.maybeCreate("java")
		}

		sourceSet.java.srcDirs(protoSourceSet.output)

		project.tasks.named(sourceSet.getTaskName("process", "resources"), ProcessResources::class.java).configure {
			it.from(protoSourceSet.proto) { cs -> cs.include("**/*.proto") }
		}

		val closure: () -> Unit = {
			project.plugins.withId("eclipse") {
				generateProtoTask.get().outputSourceDirectories.all(File::mkdirs)
			}

			project.plugins.withId("idea") {
				val isTest: Boolean = Utils.isTest(sourceSet.name)
				protoSourceSet.proto.srcDirs.forEach { protoDir ->
					Utils.addToIdeSources(project, isTest, protoDir, false)
				}
				Utils.addToIdeSources(project, isTest, project.files(extractProtosTask).singleFile, true)
				Utils.addToIdeSources(project, isTest, project.files(extractIncludeProtosTask).singleFile, true)
				generateProtoTask.get().outputSourceDirectories.forEach { outputDir ->
					Utils.addToIdeSources(project, isTest, outputDir, true)
				}
			}
		}

		postConfigure.add(closure)
	}

	private fun addGenerateProtoTask(
		protoSourceSet: ProtoSourceSet,
		configureAction: Action<GenerateProtoTask>
	): Provider<GenerateProtoTask> {
		val sourceSetName = protoSourceSet.name
		val taskName = "generate${Utils.getSourceSetSubstringForTaskNames(sourceSetName)}Proto"
		val defaultGeneratedFilesBaseDir = protobufExtension.defaultGeneratedFilesBaseDir
		val generatedFilesBaseDirProvider = protobufExtension.generatedFilesBaseDirProperty
		val task = project.tasks.register(taskName, GenerateProtoTask::class.java) { protoTask ->
			val copyActionFacade = CopyActionFacade.Loader.create(protoTask.project, protoTask.objectFactory)
			protoTask.description = "Compiles Proto source for '${sourceSetName}'"
			protoTask.setOutputBaseDir(defaultGeneratedFilesBaseDir.map { "${it}/${sourceSetName}" })
			protoTask.addSourceDirs(protoSourceSet.proto)
			protoTask.addIncludeDir(protoSourceSet.proto.sourceDirectories)
			protoTask.addIncludeDir(protoSourceSet.includeProtoDirs)
			protoTask.doLast {
				val generatedFilesBaseDir = generatedFilesBaseDirProvider.get()
				if (generatedFilesBaseDir != defaultGeneratedFilesBaseDir.get()) {
					// Purposefully don't wire this up to outputs, as it can be mixed with other files.
					copyActionFacade.copy { spec ->
						spec.includeEmptyDirs = false
						spec.from(protoTask.outputBaseDir)
						spec.into("${generatedFilesBaseDir}/${sourceSetName}")
					}
				}
			}
			configureAction.execute(protoTask)
		}
		protoSourceSet.output.from(task.map {
			it.outputSourceDirectories
		})
		return task
	}

	private fun setupExtractProtosTask(
		protoSourceSet: ProtoSourceSet,
		protobufConfig: Configuration
	): Provider<ProtobufExtract> {
		val sourceSetName = protoSourceSet.name
		val taskName = getExtractProtosTaskName(sourceSetName)

		val task = project.tasks.register(taskName, ProtobufExtract::class.java) {
			it.description = "Extracts proto files/dependencies specified by 'protobuf' configuration"
			it.destDir.set(File(getExtractedProtosDir(sourceSetName)))
			it.inputFiles.from(protobufConfig)
		}
		protoSourceSet.proto.srcDir(task)
		return task
	}

	private fun setupExtractIncludeProtosTask(
		protoSourceSet: ProtoSourceSet,
		archives: FileCollection
	): Provider<ProtobufExtract> {
		val taskName = "extractInclude${Utils.getSourceSetSubstringForTaskNames(protoSourceSet.name)}Proto"
		val task = project.tasks.register(taskName, ProtobufExtract::class.java) {
			it.description = "Extracts proto files from compile dependencies for includes"
			it.destDir.set(File(getExtractedIncludeProtosDir(protoSourceSet.name)))
			it.inputFiles.from(archives)
		}
		protoSourceSet.includeProtoDirs.from(task)
		return task
	}

	private fun getExtractProtosTaskName(sourceSetName: String): String =
		"extract${Utils.getSourceSetSubstringForTaskNames(sourceSetName)}Proto"

	private fun getExtractedIncludeProtosDir(sourceSetName: String): String =
		"${project.layout.buildDirectory.asFile.get()}/extracted-include-protos/${sourceSetName}"

	private fun getExtractedProtosDir(sourceSetName: String): String =
		"${project.layout.buildDirectory.asFile.get()}/extracted-protos/${sourceSetName}"
}
