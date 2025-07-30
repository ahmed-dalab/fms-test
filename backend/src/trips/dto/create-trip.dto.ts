import { IsNumber, IsOptional, IsString } from 'class-validator';

export class CreateTripDto {
  @IsString()
  origin: string;
  @IsString()
  destination: string;
  @IsString()
  @IsOptional()
  startTime?: Date;
  @IsString()
  @IsOptional()
  endTime?: Date;
  @IsString()
  status: string;
  @IsNumber()
  price: number;
  @IsString()
  @IsOptional()
  truckId?: string;
  @IsString()
  @IsOptional()
  driverId?: string;
  @IsString()
  @IsOptional()
  tenantId: string;
}
