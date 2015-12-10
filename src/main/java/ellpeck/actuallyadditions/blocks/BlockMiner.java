/*
 * This file ("BlockMiner.java") is part of the Actually Additions Mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://github.com/Ellpeck/ActuallyAdditions/blob/master/README.md
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015 Ellpeck
 */

package ellpeck.actuallyadditions.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ellpeck.actuallyadditions.ActuallyAdditions;
import ellpeck.actuallyadditions.blocks.base.BlockContainerBase;
import ellpeck.actuallyadditions.inventory.GuiHandler;
import ellpeck.actuallyadditions.tile.TileEntityMiner;
import ellpeck.actuallyadditions.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockMiner extends BlockContainerBase{

    @SideOnly(Side.CLIENT)
    private IIcon frontIcon;
    @SideOnly(Side.CLIENT)
    private IIcon topIcon;

    public BlockMiner(String name){
        super(Material.rock, name);
        this.setHarvestLevel("pickaxe", 0);
        this.setHardness(8F);
        this.setResistance(30F);
        this.setStepSound(soundTypeStone);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta){
        return side == 0 ? this.frontIcon : (side == 1 ? this.topIcon : this.blockIcon);
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.rare;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconReg){
        this.blockIcon = iconReg.registerIcon(ModUtil.MOD_ID_LOWER+":"+this.getBaseName());
        this.frontIcon = iconReg.registerIcon(ModUtil.MOD_ID_LOWER+":"+this.getBaseName()+"Front");
        this.topIcon = iconReg.registerIcon(ModUtil.MOD_ID_LOWER+":"+this.getBaseName()+"Top");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i){
        return new TileEntityMiner();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6){
        this.dropInventory(world, x, y, z);
        super.breakBlock(world, x, y, z, block, par6);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
        if(!world.isRemote){
            TileEntity tile = world.getTileEntity(x, y, z);
            if(tile != null && tile instanceof TileEntityMiner){
                if(player.isSneaking()){
                    player.addChatComponentMessage(new ChatComponentText(((TileEntityMiner)tile).storage.getEnergyStored()+"/"+((TileEntityMiner)tile).storage.getMaxEnergyStored()+" RF"));

                    String info = ((TileEntityMiner)tile).layerAt <= 0 ? "Done Mining!" : "Mining at Y = "+((TileEntityMiner)tile).layerAt+".";
                    player.addChatComponentMessage(new ChatComponentText(info));
                }
                else{
                    player.openGui(ActuallyAdditions.instance, GuiHandler.GuiTypes.MINER.ordinal(), world, x, y, z);
                }
            }
        }
        return true;
    }
}