import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { userInfo } from "../models/userInfo.model";
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

@Injectable()
export class SaveUserInfoService {
    public userInfo: userInfo;

     constructor () { }
}