import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs/BehaviorSubject'
import { CookieService } from 'ngx-cookie-service';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

@Injectable()
export class LoginService {
    loggedIn = new BehaviorSubject<boolean>(this.isLoggedIn());
     constructor (private http: HttpClient,
                  private cookieService: CookieService) { 
    }

/**
 * Returns a User object that contains information used throughout
 * the program, including their user info, recipes, and viewed recipe
 * history. This is a pretty rudimentary login function. It calls the login 
 * function on the server, verifies that the user's credentials
 * are correct, and returns the user's information if the login is
 * successful.
 *
 * @param  user a JSON object containing the username and password of the user.
 * @return      an object containing the user's user info, their created recipes, and their viewing history.
 */

     loginUser(User: JSON) {
         this.loggedIn.next(true);
        return this.http.post('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/user/login', User);
    }

    isLoggedIn() {
        return this.cookieService.check('username');
    }
}