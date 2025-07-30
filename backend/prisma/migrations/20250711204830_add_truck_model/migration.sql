-- CreateEnum
CREATE TYPE "TruckStatus" AS ENUM ('ACTIVE', 'INACTIVE');

-- CreateTable
CREATE TABLE "Truck" (
    "id" UUID NOT NULL,
    "plateNumber" TEXT NOT NULL,
    "model" TEXT NOT NULL,
    "status" "TruckStatus" NOT NULL,
    "capacity" DOUBLE PRECISION,
    "tenantId" UUID NOT NULL,

    CONSTRAINT "Truck_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "Truck_plateNumber_key" ON "Truck"("plateNumber");

-- AddForeignKey
ALTER TABLE "Truck" ADD CONSTRAINT "Truck_tenantId_fkey" FOREIGN KEY ("tenantId") REFERENCES "Tenant"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
