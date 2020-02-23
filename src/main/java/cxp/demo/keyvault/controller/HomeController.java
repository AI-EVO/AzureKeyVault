package cxp.demo.keyvault.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cxp.demo.keyvault.utils.AzureKeyVaultUtils;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String Index(){
        return "";
    }

    @RequestMapping("/Secret/{name}")
    public String GetSecret(@PathVariable("name") String name){
        return AzureKeyVaultUtils.GetSecret(name).value();
    }
}
