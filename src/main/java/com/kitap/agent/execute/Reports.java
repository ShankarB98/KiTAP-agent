package com.kitap.agent.execute;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import static java.io.File.separator;

/**
 * Class used to change logo and name for the reports
 * @author KT1450
 */
@Slf4j
public class Reports {
    /**
     * Method which is used to change the logo in the report
     * @param path source path of the logo
     */
     public void changeLogo(String path) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            log.info("changing the logo in report");
            try {
                File src = new File(path + separator + "serenity-logo.png");
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
                }

                dest.delete();
                Files.copy(src.toPath(), dest.toPath());

                String reportFilePath = path + separator + "target" + separator + "site" + separator + "serenity";

                File[] files = new File(reportFilePath).listFiles();

                assert files != null;
                for(File file: files){
                    if(file.getName().endsWith("html")){
                        changeName(file.getAbsolutePath());
                    }
                }
                log.info("logo changed in report");
                stopWatch.stop();
                log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                        " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }

    /**
     * Method used to change the name of the report
     * @param reportFilePath path of the report
     */
    private void changeName(String reportFilePath){
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            log.info("changing the name by using reportFilePath as input");
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
                log.info("name changed for report");
                stopWatch.stop();
                log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                        " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}