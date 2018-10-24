import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { user } from '../../models/user.model';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  User: user = <user>{};
  username: string;
  password: string;

  constructor(private loginService: LoginService) { }

  ngOnInit() {
    this.User.username = this.username;
    this.User.password = this.password;
  }

  loginUser() {
    var tmp = JSON.stringify(this.User);
    var userInfo = JSON.parse(tmp);
    this.loginService.loginUser(userInfo)
      .subscribe((data: Object) => {
        alert(data);
      });
  }
}
