package gbank.io.savehandlers;

import java.util.function.Supplier;

import gbank.types.DefaultAccountBuilder;

public class DefaultAccountSaveHandler extends AccountSaveHandler {

	public DefaultAccountSaveHandler() {
		this(() -> new DefaultAccountBuilder());
	}

	public DefaultAccountSaveHandler(Supplier<DefaultAccountBuilder> createBuilder) {
		super(() -> createBuilder.get());

		// set all of the handlers
		addHandler("Principal",
				(builder, string) -> {
					DefaultAccountBuilder builderCast = (DefaultAccountBuilder) builder;
					builderCast.setPrincipal(Double.valueOf(string));
				},
				(account) -> {
					return String.valueOf(account.getBalance());
				});

		addHandler("Rate",
				(builder, string) -> {
					DefaultAccountBuilder builderCast = (DefaultAccountBuilder) builder;
					builderCast.setRate(Double.valueOf(string));
				},
				(account) -> {
					return String.valueOf(account.getRate());
				});

		addHandler("Compound_Rate",
				(builder, string) -> {
					DefaultAccountBuilder builderCast = (DefaultAccountBuilder) builder;
					builderCast.setCompoundRate(Double.valueOf(string));
				},
				(account) -> {
					return String.valueOf(account.getCompoundRate());
				});

		addHandler("Last_Compound_Time",
				(builder, string) -> {
					DefaultAccountBuilder builderCast = (DefaultAccountBuilder) builder;
					builderCast.setLastCompoundTime(Long.valueOf(string));
				},
				(account) -> {
					return String.valueOf(account.getLastCompoundTime());
				});

		addHandler("Name",
				(builder, string) -> {
					DefaultAccountBuilder builderCast = (DefaultAccountBuilder) builder;
					builderCast.setName(string);
				},
				(account) -> {
					String name = account.getName();
					if (name.equals(""))
						return null;
					return name;
				});
		
		addHandler("ID",
				(builder, string) -> {
					DefaultAccountBuilder builderCast = (DefaultAccountBuilder) builder;
					builderCast.setAccountID(Integer.valueOf(string));
				},
				(account) -> {
					int id = account.getAccountID();
					return String.valueOf(id);
				});
	}

}
