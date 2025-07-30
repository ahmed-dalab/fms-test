import { Injectable } from '@nestjs/common';
import { UsersService } from 'src/users/users.service';
import * as bcrypt from 'bcrypt';
import { Role } from '@prisma/client';
import { JwtService } from '@nestjs/jwt';
import {
  LoginUserDto,
  RegisterResponseDto,
  RegisterUserDto,
} from './dto/auth-dto';
import * as crypto from 'crypto';
import { addMinutes } from 'date-fns'; // or use dayjs
import { UserAlreadyExistsException } from './exceptions/user-already-exists.exception';
import { InvalidRoleException } from './exceptions/invalid-role.exception';
import { UserNotFoundException } from './exceptions/user-not-found.exception';
import { InvalidPasswordException } from './exceptions/invalid-password.exception';
import { InvalidOrExpiredTokenException } from './exceptions/invalid-or-expired-token.exception';

@Injectable()
export class AuthService {
  constructor(
    private userService: UsersService,
    private readonly jwtService: JwtService,
  ) {}
  // register user
  async register(data: RegisterUserDto, tenantId?: string) {
    console.log('tenantId: ', tenantId);
    console.log('tenantId in data: ', data.tenantId);

    // check if user already exists
    const existingUser = await this.userService.findUserByEmail(data.email);
    if (existingUser) {
      throw new UserAlreadyExistsException();
    }

    if (
      data?.role &&
      !Object.values(Role).includes(data?.role.toUpperCase() as Role)
    ) {
      throw new InvalidRoleException();
    }

    // hash the password
    const hashedPassword = await bcrypt.hash(data.password, 10);

    // determine role (default to DRIVER if not provided)
    const role = (data.role?.toUpperCase() as Role) || 'DRIVER';

    // create user
    const userData: any = {
      ...data,
      password: hashedPassword,
      role,
      tenantId: role === 'SUPER_ADMIN' ? null : data.tenantId,
    };

    const newUser: RegisterResponseDto =
      await this.userService.createUser(userData);
    return { message: 'User registered successfully', user: newUser };
  }
  // login user
  async login(data: LoginUserDto) {
    // check if user exists
    const user = await this.userService.findUserByEmail(data.email);
    if (!user) {
      throw new UserNotFoundException();
    }
    // check if password is correct
    const isPasswordValid = await bcrypt.compare(data.password, user.password);
    if (!isPasswordValid) {
      throw new InvalidPasswordException();
    }
    // return user data without password
    const { password: _, ...userData } = user;
    const payload = {
      sub: user.id,
      email: user.email,
      role: user.role,
      tenantId: user.tenantId,
    };
    const token = this.jwtService.sign(payload);
    return { token };
  }

  async forgotPassword(email: string) {
    const user = await this.userService.findUserByEmail(email);
    if (!user) throw new UserNotFoundException();

    const token = crypto.randomBytes(32).toString('hex');
    const expiry = addMinutes(new Date(), 15); // token valid for 15 mins

    await this.userService.updateUser(user.id, {
      resetToken: token,
      resetTokenExpiry: expiry,
    });

    // You'd send this token via email in real life
    const resetLink = `http://localhost:3000/auth/reset-password?token=${token}`;

    return {
      message: 'Password reset link generated',
      resetLink,
    };
  }
  async resetPassword(token: string, newPassword: string) {
    const user = await this.userService.findUserByResetToken(token);

    if (
      !user ||
      !user.resetTokenExpiry ||
      user.resetTokenExpiry.getTime() < Date.now()
    ) {
      throw new InvalidOrExpiredTokenException();
    }

    const hashedPassword = await bcrypt.hash(newPassword, 10);

    await this.userService.updateUser(user.id, {
      password: hashedPassword,
      resetToken: null,
      resetTokenExpiry: null,
    });

    return { message: 'Password has been reset successfully' };
  }
}
