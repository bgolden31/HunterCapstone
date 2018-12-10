import { Component, OnInit } from '@angular/core';
import { RegisterService } from '../../services/register.service';
import { userRegister } from '../../models/registeruser.model';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  User: userRegister = <userRegister>{};
  user_id: number;
  username: string;
  password: string;
  email: string;
  age: number;
  name: string;

  constructor(private registerService: RegisterService) { }

  ngOnInit() {
  }


/**
 * This function is called when the user wants
 * to register an account. The values for their
 * name, age, email, username, and password are
 * stored in the User object, which is then parsed
 * into a JSON object and passed as a parameter
 * to the service.
 */

  registerUser() {
    this.User.username = this.username;
    this.User.password = this.password;
    this.User.email = this.email;
    this.User.age = this.age;
    this.User.name = this.name;
    var tmp = JSON.stringify(this.User);
    var userInfo = JSON.parse(tmp);
    this.registerService.registerUser(userInfo)
      .subscribe((data: Object) => {
        alert(data)
      });
  }
}
