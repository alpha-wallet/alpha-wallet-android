package io.stormbird.wallet.interact;

import android.app.Activity;
import io.reactivex.Completable;
import io.stormbird.wallet.entity.Wallet;
import io.stormbird.wallet.entity.WalletType;
import io.stormbird.wallet.repository.WalletRepositoryType;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.stormbird.wallet.service.HDKeyService;

/**
 * Delete and fetchTokens wallets
 */
public class DeleteWalletInteract {
	private final WalletRepositoryType walletRepository;

	public DeleteWalletInteract(WalletRepositoryType walletRepository) {
		this.walletRepository = walletRepository;
	}

	public Single<Wallet[]> delete(Wallet wallet, Activity activity)
	{
		if (wallet.type == WalletType.HDKEY)
		{
			return Single.fromCallable(() -> {
				HDKeyService svs = new HDKeyService(activity);
				svs.deleteHDKey(wallet.address);
				return wallet.address;
			}).flatMap(walletRepository::deleteWalletFromRealm)
					.flatMapCompletable(addr -> walletRepository.deleteWallet(addr, ""))
					.andThen(walletRepository.fetchWallets())
			  .observeOn(AndroidSchedulers.mainThread());
		}
		else if (wallet.type == WalletType.WATCH)
		{
			return walletRepository.deleteWalletFromRealm(wallet.address)
					.flatMapCompletable(addr -> walletRepository.deleteWallet(addr, ""))
					.andThen(walletRepository.fetchWallets())
					.observeOn(AndroidSchedulers.mainThread());
		}
		else
		{
			return Single.fromCallable(() -> {
				HDKeyService svs = new HDKeyService(activity);
				svs.deleteKeystoreKey(wallet.address);
				return wallet.address;
			}).flatMap(walletRepository::deleteWalletFromRealm)
					.flatMapCompletable(addr -> walletRepository.deleteWallet(addr, ""))
					.andThen(walletRepository.fetchWallets())
					.observeOn(AndroidSchedulers.mainThread());
		}
	}
}
