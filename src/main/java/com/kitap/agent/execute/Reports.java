package com.kitap.agent.execute;

import com.kitap.agent.util.PropertyReader;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import static java.io.File.separator;

public class Reports {

        public void changeLogo(String path) {
            try {
                File src = new File(path + separator + "serenity-logo.png");
                //final URL kairosLogo = this.getClass().getResource("/images/serenity-logo.png");
                //final String kairosLogo = System.getProperty("user.dir")+"\\src\\main\\resources\\images\\serenity-logo.png";
                //System.out.println(kairosLogo.toString());
                //File src = new File(kairosLogo.getFile());
                File dest = new File(path + separator + "target" + separator + "site" + separator + "serenity" + separator + "images" + separator + "serenity-logo.png");

                long Old = dest.lastModified();
                long Present = new Date().getTime();
                long Diff = Present - Old;

                System.out.println("First diff");
                System.out.println(Present - Old);

                int count = 0;
                while (Diff > 100000) {
                    Old = dest.lastModified();
                    Present = new Date().getTime();
                    Diff = Present - Old;
                    System.out.println(Diff);
                    count = count + 1;
                    System.out.println(count);
                   /* if(count > 100){
                        break;
                    }*/
                }

                dest.delete();
                Files.copy(src.toPath(), dest.toPath());

                //String reportFilePath = path + separator + "target" + separator + "site" + separator + "serenity" + separator + "index.html";
                String reportFilePath = path + separator + "target" + separator + "site" + separator + "serenity";

                File[] files = new File(reportFilePath).listFiles();

                for(File file: files){
                    if(file.getName().endsWith("html")){
                        changeName(file.getAbsolutePath());
                    }
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }


        private void changeName(String reportFilePath){
            try {
                Path reportPath = Path.of(reportFilePath);
                String str = Files.readString(reportPath);

                str = str.replace("<span class=\"projecttitle\">Serenity BDD Report</span>", "<span class=\"projecttitle\">KiTAP Report</span>");
                str = str.replace("<span class=\"projecttitle\"></span>", "<span class=\"projecttitle\">KiTAP Report</span>");
                str = str.replace("<span class=\"projecttitle\">Serenity</span>", "<span class=\"projecttitle\">KiTAP Report</span>");


                str = str.replace("<span class=\"version\">Serenity BDD version 3.2.5</span>", "<span class=\"version\">KiTAP version 1.0</span>");
                str = str.replace("<span class=\"version\">Serenity BDD version 3.3.2</span>", "<span class=\"version\">KiTAP version 1.0</span>");
                str = str.replace("<span class=\"version\">Serenity BDD version 3.3.0</span>", "<span class=\"version\">KiTAP version 1.0</span>");



                File report = new File(reportFilePath);
                report.delete();
                report.createNewFile();

                Files.writeString(reportPath, str);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


}