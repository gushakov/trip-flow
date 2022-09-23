package com.github.tripflow.infrastructure.adapter.web.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@Slf4j
public class TaskController {

    /**
     * Redirects request to the path provided {@code action} parameter. Propagates
     * {@code taskId} request parameter to the redirected endpoint.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/performTaskAction",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String performTaskAction(@RequestParam String action, @RequestParam String taskId) {
        log.debug("[POST][Task] Redirect on action: {}", action);
        return "redirect:" + UriComponentsBuilder.fromPath(action)
                .queryParam("taskId", taskId)
                .toUriString();

    }

}
