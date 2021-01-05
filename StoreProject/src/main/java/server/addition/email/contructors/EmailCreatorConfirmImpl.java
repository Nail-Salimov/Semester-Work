package server.addition.email.contructors;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Component("confirm_message_creator")
public class EmailCreatorConfirmImpl implements EmailCreator {

    @Override
    public String createEmail(String... args) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        try {
            cfg.setDirectoryForTemplateLoading(new File(
                    "/home/nail/Progy/JavaLab/JavaLab11/src/main/webapp/templates/"));
            Template template = cfg.getTemplate("confirm_message.ftl");


            Map<String, Object> root = new HashMap<>();


            root.put("name", args[0]);
            root.put("mail", args[1]);
            root.put("t", args[2]);
            root.put("u", args[3]);
            root.put("m", args[4]);

            Writer out = new StringWriter();
            template.process(root, out);

            return out.toString();
        } catch (IOException | TemplateException e){
           throw  new IllegalArgumentException(e);
        }

    }
}
