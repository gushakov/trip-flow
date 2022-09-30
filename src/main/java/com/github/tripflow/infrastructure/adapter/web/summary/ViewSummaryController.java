package com.github.tripflow.infrastructure.adapter.web.summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor

public class ViewSummaryController {

    private final ApplicationContext applicationContext;

}
