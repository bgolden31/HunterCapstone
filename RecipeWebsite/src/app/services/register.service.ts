import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

@Injectable()
export class RegisterService {
     constructor (private http: HttpClient) { }

/**
 * Returns a string telling notifying the user whether their
 * account registration was successful or not. The user inputs
 * their desired username, their age, their name, and their email,
 * and then the database is checked to see if the username or email
 * already exists. If not, the registration is successful.
 *
 * @param  user a JSON object containing the user's info for registration.
 * @return      a string expressing whether or not the registration was complete.
 */

     registerUser(User: JSON) {
        return this.http.post('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/user/register', User, {responseType: 'text'});
    }
}