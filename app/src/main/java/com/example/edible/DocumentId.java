package com.example.edible;

import androidx.annotation.NonNull;

public class DocumentId {
    public String docId;
    public <T extends DocumentId>T withID(@NonNull final String id){
        this.docId = id;
        return (T)this;
    }
}
