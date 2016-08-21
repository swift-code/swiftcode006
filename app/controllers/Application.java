package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import forms.LoginForm;
import forms.SignupForm;
import models.Profile;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * Created by lubuntu on 8/21/16.
 */

public class Application extends Controller {
    @Inject
    FormFactory formFactory;
    @Inject
    ObjectMapper objectMapper;

    public Result signup(){
        Form<SignupForm>sform = formFactory.form(SignupForm.class).bindFromRequest();
        if(sform.hasErrors())
        {

            return ok(sform.errorsAsJson());
        }
        Profile profile=new Profile(sform.data().get("firstName"),sform.data().get("lastName"));
        Profile.db().save(profile);
        User user=new User(sform.data().get("email"),sform.data().get("password"));
        user.profile=profile;
        User.db().save(user);
        return ok((JsonNode)objectMapper.valueToTree(user));
    }

    public Result login()
    {
     Form<LoginForm>lform = formFactory.form(LoginForm.class).bindFromRequest();
        if(lform.hasErrors())
        {
            return ok(lform.errorsAsJson());
        }

        User user=new User(lform.data().get("email"),lform.data().get("password"));
        return ok((JsonNode)objectMapper.valueToTree(user));
     }

}
