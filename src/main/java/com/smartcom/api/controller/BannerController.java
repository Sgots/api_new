package com.smartcom.api.controller;

import com.smartcom.api.model.Announcements;
import com.smartcom.api.model.Banner;
import com.smartcom.api.model.Devices;
import com.smartcom.api.repository.BannerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class BannerController {

    @Autowired
    BannerRepo imageRepository;

    @PostMapping("admin/upload")
    public ResponseEntity<?> uplaodImage(@RequestParam("imageFile") MultipartFile file) throws IOException {
        System.out.println("Original Image Byte Size - " + file.getBytes().length);
        Banner img = new Banner(file.getOriginalFilename(), file.getContentType(),
                compressBytes(file.getBytes()));
        img.setDeleteStatus(false);
        imageRepository.save(img);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false"));
    }

    @GetMapping(path = {"admin/get/{imageName}"})
    public Banner getImage(@PathVariable("imageName") String imageName) throws IOException {
        final Optional<Banner> retrievedImage = imageRepository.findByName(imageName);
        Banner img = new Banner(retrievedImage.get().getName(), retrievedImage.get().getType(),
                decompressBytes(retrievedImage.get().getPicByte()));
        return img;
    }

    @GetMapping(path = {"admin/banners/"})
    public ResponseEntity<?> getAllImage() throws IOException {
        List<Banner> bannerList = imageRepository.findAll();
        //   final Optional<Banner> retrievedImage = imageRepository.findByName(imageName);
       /* Banner img = new Banner(retrievedImage.get().getName(), retrievedImage.get().getType(),
                decompressBytes(retrievedImage.get().getPicByte()));*/
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",
                "message", "retrieved",
                "banners", bannerList)
        );
    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }

    @PutMapping(path = {"/admin/bannerDelete"})
    public ResponseEntity<?> announceDelete(@Valid @RequestBody Banner banner) {
        if (imageRepository.findById(banner.getId()) == null) {
            return new ResponseEntity((Object) ("Unable to update as  Announcement id " + banner.getId() + " not found."), HttpStatus.NOT_FOUND);
        }
        Announcements announcements1 = new Announcements();
        Integer id = banner.getId();

        imageRepository.findByBanneridAndDelete(id);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false"));

    }

    @GetMapping("/product/display/{id}")
    @ResponseBody
    void showImage(@PathVariable("id") Integer id, HttpServletResponse response, Optional<Banner> product)
            throws ServletException, IOException {
        // log.info("Id :: " + id);
        product = imageRepository.findById(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(product.get().getPicByte());
        response.getOutputStream().close();
    }
}
