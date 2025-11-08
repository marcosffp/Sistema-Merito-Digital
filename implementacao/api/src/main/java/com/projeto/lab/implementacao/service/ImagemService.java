package com.projeto.lab.implementacao.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.projeto.lab.implementacao.exception.VantagemException;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class ImagemService {
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile image, String folder) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new VantagemException("A imagem fornecida está vazia ou é inválida");
        }
        Map<String, Object> uploadOptions = new java.util.HashMap<>();
        uploadOptions.put("folder", folder);
        uploadOptions.put("resource_type", "auto");

        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(image.getBytes(), uploadOptions);

        String secureUrl = (String) uploadResult.get("secure_url");
        if (secureUrl == null || secureUrl.isEmpty()) {
            throw new VantagemException("Erro ao obter a URL segura da imagem");
        }

        return secureUrl;
    }

    public void deleteImage(String imageUrl) {
        if (imageUrl != null && imageUrl.contains("cloudinary.com")) {
            try {
                String publicId = extractPublicIdFromUrl(imageUrl);
                if (publicId != null) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> destroyResult = (Map<String, Object>) cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                    System.out.println("Resultado da exclusão: " + destroyResult);
                }
            } catch (IOException e) {
                throw new VantagemException("Erro ao deletar imagem do Cloudinary", e);
            }
        }
    }

    private String extractPublicIdFromUrl(String url) {
        try {
            String[] parts = url.split("/");
            String filename = parts[parts.length - 1];
            return "vantagens/" + filename.substring(0, filename.lastIndexOf('.'));
        } catch (Exception e) {
            return null;
        }
    }
}