package spring.console.utils;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FilenameFilter;

public class Helper {


    public String findRoot(String root){
        File directory = new File(root);
        String[] directories = directory.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        String[] files = directory.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isFile();
            }
        });

        if(directories.length == 1){

            if(files.length == 0 || (files.length == 1 && files[0].matches(".DS_Store"))){

                return findRoot(root + "/" + directories[0]);
            } else {
                return root;
            }

        } else {
            return root;
        }
    }
}
