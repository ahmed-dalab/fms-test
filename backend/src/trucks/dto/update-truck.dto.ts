import { Transform } from 'class-transformer';
import {
  IsString,
  IsNumber,
  IsOptional,
  Matches,
  Length,
} from 'class-validator';

type TruckStatus = 'ACTIVE' | 'INACTIVE';
export class UpdateTruckDto {
  @IsOptional()
  @IsString()
  plateNumber?: string;

  @IsOptional()
  model?: string;

  @IsOptional()
  @IsNumber()
  capacity?: number;

  @IsOptional()
  @IsString()
  status?: TruckStatus;
  @IsOptional()
  @IsString()
  @Transform(({ value }) => value?.toUpperCase())
  @Matches(/^[A-HJ-NPR-Z0-9]{17}$/, {
    message: 'VIN must be 17 characters long and exclude I, O, and Q.',
  })
  @Length(17, 17, { message: 'VIN must be exactly 17 characters.' })
  vin?: string;
  @IsOptional()
  @IsString()
  tenantId?: string;
}
