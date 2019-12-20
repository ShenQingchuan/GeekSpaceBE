package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.services.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/{commentId}")
    public Object insertNewReply(
            @PathVariable Long commentId,
            @RequestBody JSONObject reqBody,
            @RequestParam("targetType") String targetType
    ) {
        return replyService.newReply(commentId, reqBody, targetType);
    }

}
