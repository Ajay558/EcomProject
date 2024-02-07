package com.lcwd.electronic.store.ElectronicStore.exception;

import lombok.Builder;

@Builder
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(){
        super("Resources not found !!");
    }

    public ResourceNotFoundException(String message){
        super(message);
    }

}
