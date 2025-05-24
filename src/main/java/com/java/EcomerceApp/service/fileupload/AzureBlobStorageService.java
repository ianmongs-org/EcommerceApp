package com.java.EcomerceApp.service.fileupload;

import com.azure.storage.blob.BlobContainerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@RequiredArgsConstructor
public class AzureBlobStorageService {

    private final BlobContainerClient blobContainerClient;


}
