import { IsString, IsEnum, IsOptional } from 'class-validator';
import { DriverStatus } from './create-driver.dto';

export class UpdateDriverDto {
  @IsOptional()
  @IsString()
  licenseNumber?: string;

  @IsOptional()
  @IsString()
  phone?: string;

  @IsOptional()
  @IsEnum(DriverStatus)
  status?: DriverStatus;
}

