import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  loggedIn: boolean = false;

  constructor(private router: Router,
              private cookieService: CookieService) { 
  }

  ngOnInit() {
    if (this.cookieService.check('username') == true) {
      this.loggedIn = true;
    }
  }

  logout() {
    this.cookieService.deleteAll();
    localStorage.clear();
    this.router.navigate(['/home']);
  }

  goToLogin() {
    if (this.cookieService.check('username') == true) {
      alert("You're already logged in");
    }
    else {
      this.router.navigate(['/login']);
    }
  }

  goToRegister() {
    if (this.cookieService.check('username') == true) {
      alert("You're already logged in");
    }
    else {
      this.router.navigate(['/register']);
    }
  }
}
