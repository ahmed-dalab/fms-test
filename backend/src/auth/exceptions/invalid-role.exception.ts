import { HttpException, HttpStatus } from '@nestjs/common';

export class InvalidRoleException extends HttpException {
  constructor() {
    super('Invalid role provided', HttpStatus.BAD_REQUEST);
  }
} 