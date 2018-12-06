import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  loggedIn: boolean;

  constructor(private router: Router,
              private cookieService: CookieService,
              private loginService: LoginService) { 
  }

  ngOnInit() {
    this.loginService.loggedIn.subscribe(loggedIn => this.loggedIn = loggedIn)
  }

  logout() {
    this.cookieService.deleteAll();
    localStorage.clear();
    this.loginService.loggedIn.next(false);
    this.router.navigate(['/home']);
  }

/**
 * This function does a quick check to see if the user
 * is logged in or not. This is done by checking cookies
 * for a value for the 'username' key. If there is a
 * value, then the user is logged in and is unable to log in
 * while already logged in.
 */

  goToLogin() {
    if (this.cookieService.check('username') == true) {
      alert("You're already logged in");
    }
    else {
      this.router.navigate(['/login']);
    }
  }

/**
 * Similarily to the above function, this function does 
 * a quick check to see if the user is logged in or not. 
 * This is done by checking cookies for a value for 
 * the 'username' key. If there is a value, then 
 * the user is logged in and is unable to register
 * while already logged in.
 */

  goToRegister() {
    if (this.cookieService.check('username') == true) {
      alert("You're already logged in");
    }
    else {
      this.router.navigate(['/register']);
    }
  }
}
