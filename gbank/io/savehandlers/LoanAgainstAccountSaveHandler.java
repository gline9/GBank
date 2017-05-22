package gbank.io.savehandlers;

import java.util.function.Supplier;

import gbank.types.LoanAgainstAccount;
import gbank.types.LoanAgainstAccountBuilder;

public class LoanAgainstAccountSaveHandler extends DefaultAccountSaveHandler {
	public LoanAgainstAccountSaveHandler(Supplier<LoanAgainstAccountBuilder> createBuilder) {
		super(() -> createBuilder.get());

		// set all of the handlers for this type of account
		addHandler("Maximum_Owed",
				(builder, string) -> {
					LoanAgainstAccountBuilder builderCast = (LoanAgainstAccountBuilder) builder;
					builderCast.setMaximumOwed(Double.valueOf(string));
				},
				(account) -> {
					LoanAgainstAccount accountCast = (LoanAgainstAccount) account;
					return String.valueOf(accountCast.getMaximumOwed());
				});
		
		addHandler("Against_ID",
				(builder, string) -> {
					LoanAgainstAccountBuilder builderCast = (LoanAgainstAccountBuilder) builder;
					builderCast.setAgainstID(Integer.valueOf(string));
				},
				(account) -> {
					LoanAgainstAccount accountCast = (LoanAgainstAccount) account;
					return String.valueOf(accountCast.getAgainstID());
				});
	}
}
