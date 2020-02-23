package cxp.demo.keyvault.controller;

import cxp.demo.keyvault.utils.AzureKeyVaultUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String Index(){
        return "";
    }

    @RequestMapping("/Secret/{name}")
    public String GetSecret(@PathVariable("name") String name){
        return AzureKeyVaultUtil.GetSecret(name).value();
    }
}
