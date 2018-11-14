import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private router: Router,
              private cookieService: CookieService) { 
  }

  ngOnInit() {
  }

  goToLogin() {
    if (this.cookieService.check('username') == true) {
      alert("You're already logged in");
    }
    else {
      this.router.navigate(['/login']);
    }
  }
}
