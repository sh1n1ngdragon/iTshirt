package com.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/map")
@Controller
@RequiredArgsConstructor
public class MapController {

   @GetMapping(value = "/location")
   public String viewLocation(){
      return "map/location";
   }
}
