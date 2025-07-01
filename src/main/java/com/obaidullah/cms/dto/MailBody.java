package com.obaidullah.cms.dto;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}
