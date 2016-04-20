package twilightforest.tileentity;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import twilightforest.block.TFBlocks;
import twilightforest.entity.EntityTFMiniGhast;
import com.gamerforea.eventhelper.util.EventUtils;

public class TileEntityTFCReactorActive extends TileEntity {

   int counter = 0;
   int secX;
   int secY;
   int secZ;
   int terX;
   int terY;
   int terZ;


   public TileEntityTFCReactorActive() {
      Random rand = new Random();
      this.secX = 3 * (rand.nextBoolean()?1:-1);
      this.secY = 3 * (rand.nextBoolean()?1:-1);
      this.secZ = 3 * (rand.nextBoolean()?1:-1);
      this.terX = 3 * (rand.nextBoolean()?1:-1);
      this.terY = 3 * (rand.nextBoolean()?1:-1);
      this.terZ = 3 * (rand.nextBoolean()?1:-1);
      if(this.secX == this.terX && this.secY == this.terY && this.secZ == this.terZ) {
         this.terX = -this.terX;
         this.terY = -this.terY;
         this.terZ = -this.terZ;
      }

   }

   public void updateEntity() {
      ++this.counter;
      if(!super.worldObj.isRemote) {
         byte offset = 10;
         int i;
         if(this.counter % 5 == 0) {
            if(this.counter == 5) {
               super.worldObj.setBlock(super.xCoord + 1, super.yCoord + 1, super.zCoord + 1, TFBlocks.towerTranslucent, 7, 2);
               super.worldObj.setBlock(super.xCoord + 1, super.yCoord + 1, super.zCoord - 1, TFBlocks.towerTranslucent, 7, 2);
               super.worldObj.setBlock(super.xCoord - 1, super.yCoord + 1, super.zCoord + 1, TFBlocks.towerTranslucent, 7, 2);
               super.worldObj.setBlock(super.xCoord - 1, super.yCoord + 1, super.zCoord - 1, TFBlocks.towerTranslucent, 7, 2);
               super.worldObj.setBlock(super.xCoord + 0, super.yCoord + 1, super.zCoord + 1, TFBlocks.towerTranslucent, 6, 2);
               super.worldObj.setBlock(super.xCoord + 0, super.yCoord + 1, super.zCoord - 1, TFBlocks.towerTranslucent, 6, 2);
               super.worldObj.setBlock(super.xCoord + 1, super.yCoord + 1, super.zCoord + 0, TFBlocks.towerTranslucent, 6, 2);
               super.worldObj.setBlock(super.xCoord - 1, super.yCoord + 1, super.zCoord + 0, TFBlocks.towerTranslucent, 6, 2);
               super.worldObj.setBlock(super.xCoord + 1, super.yCoord + 0, super.zCoord + 1, TFBlocks.towerTranslucent, 6, 2);
               super.worldObj.setBlock(super.xCoord + 1, super.yCoord + 0, super.zCoord - 1, TFBlocks.towerTranslucent, 6, 2);
               super.worldObj.setBlock(super.xCoord - 1, super.yCoord + 0, super.zCoord + 1, TFBlocks.towerTranslucent, 6, 2);
               super.worldObj.setBlock(super.xCoord - 1, super.yCoord + 0, super.zCoord - 1, TFBlocks.towerTranslucent, 6, 2);
               super.worldObj.setBlock(super.xCoord + 0, super.yCoord + 0, super.zCoord + 1, TFBlocks.towerTranslucent, 7, 2);
               super.worldObj.setBlock(super.xCoord + 0, super.yCoord + 0, super.zCoord - 1, TFBlocks.towerTranslucent, 7, 2);
               super.worldObj.setBlock(super.xCoord + 1, super.yCoord + 0, super.zCoord + 0, TFBlocks.towerTranslucent, 7, 2);
               super.worldObj.setBlock(super.xCoord - 1, super.yCoord + 0, super.zCoord + 0, TFBlocks.towerTranslucent, 7, 2);
               super.worldObj.setBlock(super.xCoord + 1, super.yCoord - 1, super.zCoord + 1, TFBlocks.towerTranslucent, 7, 2);
               super.worldObj.setBlock(super.xCoord + 1, super.yCoord - 1, super.zCoord - 1, TFBlocks.towerTranslucent, 7, 2);
               super.worldObj.setBlock(super.xCoord - 1, super.yCoord - 1, super.zCoord + 1, TFBlocks.towerTranslucent, 7, 2);
               super.worldObj.setBlock(super.xCoord - 1, super.yCoord - 1, super.zCoord - 1, TFBlocks.towerTranslucent, 7, 2);
               super.worldObj.setBlock(super.xCoord + 0, super.yCoord - 1, super.zCoord + 1, TFBlocks.towerTranslucent, 6, 2);
               super.worldObj.setBlock(super.xCoord + 0, super.yCoord - 1, super.zCoord - 1, TFBlocks.towerTranslucent, 6, 2);
               super.worldObj.setBlock(super.xCoord + 1, super.yCoord - 1, super.zCoord + 0, TFBlocks.towerTranslucent, 6, 2);
               super.worldObj.setBlock(super.xCoord - 1, super.yCoord - 1, super.zCoord + 0, TFBlocks.towerTranslucent, 6, 2);
            }

            i = this.counter - 80;
            if(i >= offset && i <= 249) {
               this.drawBlob(super.xCoord, super.yCoord, super.zCoord, (i - offset) / 40, Blocks.air, 0, i - offset, false);
            }

            if(i <= 200) {
               this.drawBlob(super.xCoord, super.yCoord, super.zCoord, i / 40, TFBlocks.towerTranslucent, 5, this.counter, false);
            }

            int secondary = this.counter - 120;
            if(secondary >= offset && secondary <= 129) {
               this.drawBlob(super.xCoord + this.secX, super.yCoord + this.secY, super.zCoord + this.secZ, (secondary - offset) / 40, Blocks.air, 0, secondary - offset, false);
            }

            if(secondary >= 0 && secondary <= 160) {
               this.drawBlob(super.xCoord + this.secX, super.yCoord + this.secY, super.zCoord + this.secZ, secondary / 40, Blocks.air, 0, secondary, true);
            }

            int tertiary = this.counter - 160;
            if(tertiary >= offset && tertiary <= 129) {
               this.drawBlob(super.xCoord + this.terX, super.yCoord + this.terY, super.zCoord + this.terZ, (tertiary - offset) / 40, Blocks.air, 0, tertiary - offset, false);
            }

            if(tertiary >= 0 && tertiary <= 160) {
               this.drawBlob(super.xCoord + this.terX, super.yCoord + this.terY, super.zCoord + this.terZ, tertiary / 40, Blocks.air, 0, tertiary, true);
            }
         }

         if(this.counter >= 350) {
            for(i = 0; i < 3; ++i) {
               this.spawnGhastNear(super.xCoord + this.secX, super.yCoord + this.secY, super.zCoord + this.secZ);
               this.spawnGhastNear(super.xCoord + this.terX, super.yCoord + this.terY, super.zCoord + this.terZ);
            }

            //super.worldObj.createExplosion((Entity)null, (double)super.xCoord, (double)super.yCoord, (double)super.zCoord, 2.0F, true);
			if (!EventUtils.isInPrivate(super.worldObj, super.xCoord, super.yCoord, super.zCoord)) //LeRioN fix
            super.worldObj.setBlock(super.xCoord, super.yCoord, super.zCoord, Blocks.air, 0, 3);
         }
      } else if(this.counter % 5 == 0 && this.counter <= 250) {
         super.worldObj.playSound((double)super.xCoord + 0.5D, (double)super.yCoord + 0.5D, (double)super.zCoord + 0.5D, "portal.portal", (float)this.counter / 100.0F, (float)this.counter / 100.0F, false);
      }

   }

   private void spawnGhastNear(int x, int y, int z) {
      EntityTFMiniGhast ghast = new EntityTFMiniGhast(super.worldObj);
      ghast.setLocationAndAngles((double)x - 1.5D + (double)super.worldObj.rand.nextFloat() * 3.0D, (double)y - 1.5D + (double)super.worldObj.rand.nextFloat() * 3.0D, (double)z - 1.5D + (double)super.worldObj.rand.nextFloat() * 3.0D, super.worldObj.rand.nextFloat() * 360.0F, 0.0F);
      super.worldObj.spawnEntityInWorld(ghast);
   }

   public void drawBlob(int sx, int sy, int sz, int rad, Block blockValue, int metaValue, int fuzz, boolean netherTransform) {
      for(byte dx = 0; dx <= rad; ++dx) {
         int fuzzX = (fuzz + dx) % 8;

         for(byte dy = 0; dy <= rad; ++dy) {
            int fuzzY = (fuzz + dy) % 8;

            for(byte dz = 0; dz <= rad; ++dz) {
               boolean dist = false;
               byte var15;
               if(dx >= dy && dx >= dz) {
                  var15 = (byte)(dx + (byte)((int)((double)Math.max(dy, dz) * 0.5D + (double)Math.min(dy, dz) * 0.25D)));
               } else if(dy >= dx && dy >= dz) {
                  var15 = (byte)(dy + (byte)((int)((double)Math.max(dx, dz) * 0.5D + (double)Math.min(dx, dz) * 0.25D)));
               } else {
                  var15 = (byte)(dz + (byte)((int)((double)Math.max(dx, dy) * 0.5D + (double)Math.min(dx, dy) * 0.25D)));
               }

               if(var15 == rad && (dx != 0 || dy != 0 || dz != 0)) {
                  switch(fuzzX) {
                  case 0:
                     this.transformBlock(sx + dx, sy + dy, sz + dz, blockValue, metaValue, fuzzY, netherTransform);
                     break;
                  case 1:
                     this.transformBlock(sx + dx, sy + dy, sz - dz, blockValue, metaValue, fuzzY, netherTransform);
                     break;
                  case 2:
                     this.transformBlock(sx - dx, sy + dy, sz + dz, blockValue, metaValue, fuzzY, netherTransform);
                     break;
                  case 3:
                     this.transformBlock(sx - dx, sy + dy, sz - dz, blockValue, metaValue, fuzzY, netherTransform);
                     break;
                  case 4:
                     this.transformBlock(sx + dx, sy - dy, sz + dz, blockValue, metaValue, fuzzY, netherTransform);
                     break;
                  case 5:
                     this.transformBlock(sx + dx, sy - dy, sz - dz, blockValue, metaValue, fuzzY, netherTransform);
                     break;
                  case 6:
                     this.transformBlock(sx - dx, sy - dy, sz + dz, blockValue, metaValue, fuzzY, netherTransform);
                     break;
                  case 7:
                     this.transformBlock(sx - dx, sy - dy, sz - dz, blockValue, metaValue, fuzzY, netherTransform);
                  }
               }
            }
         }
      }

   }

   protected void transformBlock(int x, int y, int z, Block blockValue, int metaValue, int fuzz, boolean netherTransform) {
      Block whatsThere = super.worldObj.getBlock(x, y, z);
      int whatsMeta = super.worldObj.getBlockMetadata(x, y, z);
      if(whatsThere == Blocks.air || whatsThere.getBlockHardness(super.worldObj, x, y, z) != -1.0F) {
         if(fuzz == 0 && whatsThere != Blocks.air) {
            super.worldObj.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(whatsThere) + (whatsMeta << 12));
         }

         if(netherTransform && whatsThere != Blocks.air) {
            super.worldObj.setBlock(x, y, z, Blocks.netherrack, 0, 3);
            if(super.worldObj.getBlock(x, y + 1, z) == Blocks.air && fuzz % 3 == 0) {
               if (!EventUtils.isInPrivate(super.worldObj, x, y + 1, z)) //LeRioN fix
			   super.worldObj.setBlock(x, y + 1, z, Blocks.fire, 0, 3);
            }
         } else {
			if (!EventUtils.isInPrivate(super.worldObj, x, y, z)) //LeRioN fix
            super.worldObj.setBlock(x, y, z, blockValue, metaValue, 3);
         }

      }
   }
}
