package com.folkol;

import com.folkol.model.Content;
import java.util.Collections;
import rx.Observable;

public class VaryingContentService {
    private ContentService contentService;

    public VaryingContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void updateContent(String id, Content content) {
        contentService.updateContent(id, content);
    }

    public Content getContentSync(String id, String variant) {
        Content content = contentService.getContentSync(id);
        return applyVariant(content, variant);
    }

    public Observable<Content> getContentAsync(String id, String variant) {
        return contentService.getContentAsync(id)
                             .map(content -> applyVariant(content, variant));
    }

    Content applyVariant(Content content, String variant) {
        switch (variant) {
            case "strip":
                return new Content(content.getId(), content.getDescription(), Collections.emptyMap());
        }
        return content;
    }
}
