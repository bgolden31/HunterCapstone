import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { user } from '../../models/user.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  User: user = <user>{};
  username: string;
  password: string;

  constructor(private loginService: LoginService) { }

  ngOnInit() {
    this.User.username = this.username;
    this.User.password = this.password;
  }

  /*
  loginUser() {
    var tmp = JSON.stringify(this.User);
    var userInfo = JSON.parse(tmp);
    this.loginService.loginUser(userInfo)
      .subscribe((data: Object) => {
        alert("all good");
      });
  }
  */

}
