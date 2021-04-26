/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.controllers;

import game.utils.Global;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 *
 * @author user1
 */
public class ImgController {

    // 內容
    private HashMap<String, Image> imgPairs;

    public ImgController() {
        imgPairs = new HashMap<>();
    }

    public Image tryGetImage(String path) {
        if (!imgPairs.containsKey(path)) {
            return addImage(path);
        }
        // 找到 => 直接回傳
        return imgPairs.get(path);
    }

    private Image addImage(String path) {
        try {
            if (Global.IS_DEBUG) {
//                System.out.println("load img from: " + path);
            }
            Image img = ImageIO.read(getClass().getResource(path));
            imgPairs.put(path, img);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void clear(){
        imgPairs.clear();
    }

    // 做標記 => 標記哪些資源是這個場景開始後才載入
}
