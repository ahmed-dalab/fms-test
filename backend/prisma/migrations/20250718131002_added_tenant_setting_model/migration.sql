-- CreateEnum
CREATE TYPE "DistanceUnit" AS ENUM ('KILOMETERS', 'MILES');

-- CreateEnum
CREATE TYPE "FuelUnit" AS ENUM ('LITERS', 'GALLONS');

-- CreateTable
CREATE TABLE "TenantSetting" (
    "id" UUID NOT NULL,
    "tenantId" UUID NOT NULL,
    "distanceUnit" "DistanceUnit" NOT NULL DEFAULT 'KILOMETERS',
    "fuelUnit" "FuelUnit" NOT NULL DEFAULT 'LITERS',
    "timezone" TEXT NOT NULL DEFAULT 'Africa/Mogadishu',
    "currency" TEXT NOT NULL DEFAULT 'USD',
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "TenantSetting_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "TenantSetting_tenantId_key" ON "TenantSetting"("tenantId");

-- AddForeignKey
ALTER TABLE "TenantSetting" ADD CONSTRAINT "TenantSetting_tenantId_fkey" FOREIGN KEY ("tenantId") REFERENCES "Tenant"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
