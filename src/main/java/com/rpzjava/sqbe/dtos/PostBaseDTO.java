package com.rpzjava.sqbe.dtos;

import com.alibaba.fastjson.JSONArray;
import com.rpzjava.sqbe.exceptions.PostDataNotCompleteException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostBaseDTO {

    String title;
    JSONArray tags;
    String source;
    String content;
    String cover;

    public PostBaseDTO(String title, JSONArray tags, String source, String content, String cover) throws PostDataNotCompleteException {
        if (title == null || content == null || source == null || tags.size() == 0) {
            throw new PostDataNotCompleteException();
        }
        this.title = title;
        this.tags = tags;
        this.source = source;
        this.content = content;
        this.cover = cover;
    }
}
