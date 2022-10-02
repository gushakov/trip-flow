package com.github.tripflow.infrastructure.adapter.web.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@Slf4j
public class ContinueWorkflowController {

    /**
     * Redirects request to the path provided {@code action} parameter. Propagates
     * {@code taskId} request parameter to the redirected endpoint.
     */
    @PostMapping(value = "/performTaskAction")
    public String performTaskAction(@RequestParam String action, @RequestParam String taskId) {
        log.debug("[POST][Task] Redirect on action: {}", action);
        return "redirect:" + UriComponentsBuilder.fromPath(action)
                .queryParam("taskId", taskId)
                .toUriString();

    }

}
