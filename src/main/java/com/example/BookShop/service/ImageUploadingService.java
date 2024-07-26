package com.example.BookShop.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;

@Service
public class ImageUploadingService {

    private final String bucketName = "bookshop-31512.appspot.com";

    public String upload(MultipartFile multipartFile, String name) {
        try {
            String fileName = name.concat(getExtension(multipartFile.getOriginalFilename()));
            File file = convertToFile(multipartFile, fileName);
            String URL = uploadFile(file, fileName);
            file.delete();
            return URL;
        } catch (Exception e) {
            System.out.println(e);
            return "Image couldn't upload, Something went wrong";
        }
    }

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpg").build();
        try (InputStream inputStream = ImageUploadingService.class.getClassLoader().getResourceAsStream("BookShop.json")) {
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            storage.create(blobInfo, Files.readAllBytes(file.toPath()));
            return getDownloadUrl(bucketName, fileName);
        }
    }

    private String getDownloadUrl(String bucketName, String fileName) {
        return "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/" +
                URLEncoder.encode(fileName, java.nio.charset.StandardCharsets.UTF_8) +
                "?alt=media";
    }

    private File convertToFile(MultipartFile file, String fileName) throws IOException {
        File convFile = new File(fileName);
        try (OutputStream os = new FileOutputStream(convFile)) {
            os.write(file.getBytes());
        }
        return convFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
