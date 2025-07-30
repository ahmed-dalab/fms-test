import { Transform } from 'class-transformer';
import {
  IsNotEmpty,
  IsString,
  IsEmail,
  IsNumber,
  IsOptional,
  Length,
  Matches,
} from 'class-validator';

type TruckStatus = 'ACTIVE' | 'INACTIVE';
export class CreateTruckDto {
  @IsNotEmpty()
  @IsString()
  plateNumber: string;

  @IsNotEmpty()
  model: string;

  @IsNotEmpty()
  @IsNumber()
  capacity: number;

  @IsNotEmpty()
  @IsString()
  status: TruckStatus;

  @IsOptional()
  @Transform(({ value }) => value?.toUpperCase())
  @Matches(/^[A-HJ-NPR-Z0-9]{17}$/, {
    message: 'VIN must be 17 characters long and exclude I, O, and Q.',
  })
  @Length(17, 17, { message: 'VIN must be exactly 17 characters.' })
  vin: string;

  @IsOptional()
  @IsString()
  tenantId: string;
}
