/*
  Warnings:

  - A unique constraint covering the columns `[vin]` on the table `Truck` will be added. If there are existing duplicate values, this will fail.

*/
-- AlterTable
ALTER TABLE "Truck" ADD COLUMN     "vin" TEXT;

-- CreateIndex
CREATE UNIQUE INDEX "Truck_vin_key" ON "Truck"("vin");
