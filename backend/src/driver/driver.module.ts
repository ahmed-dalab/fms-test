import { Module } from '@nestjs/common';
import { DriverService } from './driver.service';
import { DriverController } from './driver.controller';
import { DriverRepository } from './driver.repository';
import { UsersModule } from 'src/users/users.module';

@Module({
  imports: [UsersModule],
  providers: [DriverService, DriverRepository],
  controllers: [DriverController],
})
export class DriverModule {}
