import { IsString, IsEnum } from 'class-validator';

export enum DriverStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
}

export class CreateDriverDto {
  @IsString()
  licenseNumber: string;

  @IsString()
  phone: string;

  @IsEnum(DriverStatus)
  status: DriverStatus;
}
