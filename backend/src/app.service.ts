import { Injectable } from '@nestjs/common';

@Injectable()
export class AppService {
  getHello(): string {
    return 'Hello, welcome to Fleet Management System using nestjs!';
  }
}
