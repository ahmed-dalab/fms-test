import { Module } from '@nestjs/common';
import { TrucksService } from './trucks.service';
import { TrucksController } from './trucks.controller';
import { TrucksRepository } from './trucks.repository';

@Module({
  providers: [TrucksService, TrucksRepository],
  controllers: [TrucksController],
})
export class TrucksModule {}
