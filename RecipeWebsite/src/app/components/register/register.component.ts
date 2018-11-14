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
