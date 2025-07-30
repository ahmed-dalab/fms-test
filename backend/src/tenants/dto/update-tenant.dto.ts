import {
  IsEmail,
  IsOptional,
  IsString,
  ValidateNested,
  IsEnum,
  IsNumber,
} from 'class-validator';
import { Type } from 'class-transformer';

enum DistanceUnit {
  KILOMETERS = 'KILOMETERS',
  MILES = 'MILES',
}

enum FuelUnit {
  LITERS = 'LITERS',
  GALLONS = 'GALLONS',
}

export class UpdateTenantSettingDto {
  @IsOptional()
  @IsEnum(DistanceUnit)
  distanceUnit?: DistanceUnit;

  @IsOptional()
  @IsEnum(FuelUnit)
  fuelUnit?: FuelUnit;

  @IsOptional()
  @IsString()
  timezone?: string;

  @IsOptional()
  @IsString()
  currency?: string;
}

export class UpdateTenantDto {
  @IsOptional()
  @IsString()
  name?: string;

  @IsOptional()
  @IsEmail()
  email?: string;

  @IsOptional()
  @IsString()
  phoneNumber?: string;

  @IsOptional()
  @IsString()
  logoUrl?: string;

  @IsOptional()
  @IsString()
  address?: string;

  @IsOptional()
  @IsString()
  city?: string;

  @IsOptional()
  @IsString()
  state?: string;

  @IsOptional()
  @IsString()
  zipCode?: string;

  @IsOptional()
  @IsString()
  industry?: string;

  @IsOptional()
  @IsNumber()
  companySize?: number;
}
