/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alma.archive.jportal.opencadc;

import ca.nrc.cadc.uws.Job;
import ca.nrc.cadc.uws.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author jdsant
 */
public class QueryExampleClient {

    public static void main(String[] argv) {
        BasicConfigurator.configure();

        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{
                    "resources/spring/db.xml",
                    "resources/spring/context.xml"
                }
        );

        BeanFactory factory = context;

        AsyncJobservice jobService = (AsyncJobservice) factory.getBean("asyncJobService", AsyncJobservice.class);

        Map<String, String> params = new HashMap<String, String>();

        params.put("REQUEST", "doQuery");
        params.put("LANG", "SQL");
        params.put("MAXREC", "3000");
        params.put("FORMAT", "votable");
        params.put("QUERY", "select count(*) from alma.asa_obs_science_data");

        String jobId = jobService.create(params, new Subject());
        jobService.run(jobId);

        System.out.println(jobId);

        while (!jobService.isJobFinished(jobId)) {
            System.out.println("waiting");
            Job job = jobService.findJob(jobId);
            System.out.println(job.toString());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(QueryExampleClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        Job job = jobService.findJob(jobId);
        for (Result result : job.getResultsList()) {
            String filename = result.getURL().getFile();
            System.out.println(filename);
            System.out.println(filename.substring(filename.lastIndexOf("result_")));
        }



    }
}

