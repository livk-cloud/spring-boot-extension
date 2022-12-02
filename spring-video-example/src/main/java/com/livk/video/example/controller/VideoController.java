package com.livk.video.example.controller;

import com.livk.commons.util.WebUtils;
import com.livk.video.example.support.VideoResourceHttpRequestHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * <p>
 * VideoController
 * </p>
 *
 * @author livk
 * @date 2022/11/16
 */
@RestController
@RequestMapping("video")
@RequiredArgsConstructor
public class VideoController {

    private static final String VIDEO_SUFFIX = ".mp4";
    private final VideoResourceHttpRequestHandler requestHandler;

    @GetMapping
    public void video(HttpServletRequest request,
                      HttpServletResponse response,
                      @RequestParam(defaultValue = "test") String videoName) throws Exception {
        //查询视频是否存在
        if ("test".equals(videoName)) {
            request.setAttribute(VideoResourceHttpRequestHandler.ATTR, "classpath:" + videoName + VIDEO_SUFFIX);
            requestHandler.handleRequest(request, response);
        } else {
            Map<String, Serializable> map = Map.of("msg", "视频资源不存在", "status", HttpServletResponse.SC_NOT_FOUND);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            WebUtils.out(response, map);
        }
    }
}
