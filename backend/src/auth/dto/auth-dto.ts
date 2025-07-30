import {
  IsEmail,
  IsNotEmpty,
  IsOptional,
  IsString,
  ValidateIf,
} from 'class-validator';
import { Expose, Type } from 'class-transformer';
import { Role } from '@prisma/client';

export class RegisterUserDto {
  @IsNotEmpty()
  @IsString()
  name: string;

  @IsNotEmpty()
  @IsString()
  password: string;

  @IsNotEmpty()
  @IsEmail()
  email: string;

  @IsOptional()
  role?: Role;

  /**
   * Only used when registering a new tenant admin (not for regular user creation).
   * For regular user creation, tenantId is set in the service from context/middleware.
   */
  @IsOptional()
  tenantId?: string;
}

export class LoginUserDto {
  @IsNotEmpty()
  @IsEmail()
  email: string;

  @IsNotEmpty()
  @IsString()
  password: string;
}
export class LoginResponseDto {
  token: string;
}

export class RegisterResponseDto {
  @Expose()
  id: string;
  @Expose()
  name: string;
  @Expose()
  email: string;
  @Expose()
  role: string;
  @Expose()
  tenantId?: string | null;
}
