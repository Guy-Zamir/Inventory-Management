package com.guy.inventory;
import android.content.Context;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class Data {
    private Context context;
    private boolean valid;

    public Data(Context context) {
        this.context = context;
    }

    public boolean createNewExport(final Export export) {
        Backendless.Persistence.save(export, new AsyncCallback<Export>() {
            @Override
            public void handleResponse(Export response) {
                InventoryApp.exports.add(export);
                Toast.makeText(context, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }

    public boolean editExport(int selectedItem) {
        Backendless.Persistence.save(InventoryApp.exports.get(selectedItem), new AsyncCallback<Export>() {
            @Override
            public void handleResponse(Export response) {
                Toast.makeText(context, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }

    public boolean deleteExport(final int selectedItem) {
        Backendless.Persistence.of(Export.class).remove(InventoryApp.exports.get(selectedItem), new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                InventoryApp.exports.remove(selectedItem);
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, "נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }



    public boolean createNewSale(final Sale sale) {
        Backendless.Persistence.save(sale, new AsyncCallback<Sale>() {
            @Override
            public void handleResponse(Sale response) {
                InventoryApp.sales.add(sale);
                Toast.makeText(context, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }

    public boolean editSale(int selectedItem) {
        Backendless.Persistence.save(InventoryApp.sales.get(selectedItem), new AsyncCallback<Sale>() {
            @Override
            public void handleResponse(Sale response) {
                Toast.makeText(context, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }

    public boolean deleteSale(final int selectedItem) {
        Backendless.Persistence.of(Sale.class).remove(InventoryApp.sales.get(selectedItem), new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                InventoryApp.sales.remove(selectedItem);
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, "נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }



    public boolean createNewBuy(final Buy buy) {
        Backendless.Persistence.save(buy, new AsyncCallback<Buy>() {
            @Override
            public void handleResponse(Buy response) {
                InventoryApp.buys.add(buy);
                Toast.makeText(context, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }

    public boolean editBuy(int selectedItem) {
        Backendless.Persistence.save(InventoryApp.buys.get(selectedItem), new AsyncCallback<Buy>() {
            @Override
            public void handleResponse(Buy response) {
                Toast.makeText(context, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }

    public boolean deleteBuy(final int selectedItem) {
        Backendless.Persistence.of(Buy.class).remove(InventoryApp.buys.get(selectedItem), new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                InventoryApp.buys.remove(selectedItem);
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, "נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }



    public boolean createNewClient(final Client client) {
        Backendless.Persistence.save(client, new AsyncCallback<Client>() {
            @Override
            public void handleResponse(Client response) {
                InventoryApp.clients.add(client);
                Toast.makeText(context, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }

    public boolean editClient(int selectedItem) {
        Backendless.Persistence.save(InventoryApp.clients.get(selectedItem), new AsyncCallback<Client>() {
            @Override
            public void handleResponse(Client response) {
                Toast.makeText(context, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }

    public boolean deleteClient(final int selectedItem) {
        Backendless.Persistence.of(Client.class).remove(InventoryApp.clients.get(selectedItem), new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                InventoryApp.clients.remove(selectedItem);
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, "נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }



    public boolean createNewSort(final Sort sort) {
        Backendless.Persistence.save(sort, new AsyncCallback<Sort>() {
            @Override
            public void handleResponse(Sort response) {
                InventoryApp.sorts.add(sort);
                Toast.makeText(context, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }

    public boolean editSort(int selectedItem) {
        Backendless.Persistence.save(InventoryApp.sorts.get(selectedItem), new AsyncCallback<Sort>() {
            @Override
            public void handleResponse(Sort response) {
                Toast.makeText(context, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }

    public boolean deleteSort(final int selectedItem) {
        Backendless.Persistence.of(Sort.class).remove(InventoryApp.sorts.get(selectedItem), new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                InventoryApp.sorts.remove(selectedItem);
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, "נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }



    public boolean getBuyTable(DataQueryBuilder buyBuilder) {
        Backendless.Data.of(Buy.class).find(buyBuilder, new AsyncCallback<List<Buy>>() {
            @Override
            public void handleResponse(List<Buy> response) {
                InventoryApp.buys = response;
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(context, "טרם נשרמו קניות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                valid = false;
            }
        });
        return valid;
    }

    public boolean addBuyTable(DataQueryBuilder buyBuilder) {
        Backendless.Data.of(Buy.class).find(buyBuilder, new AsyncCallback<List<Buy>>() {
            @Override
            public void handleResponse(List<Buy> response) {
                InventoryApp.buys.addAll(response);
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }

    public boolean getSaleTable(DataQueryBuilder buyBuilder) {
        Backendless.Data.of(Sale.class).find(buyBuilder, new AsyncCallback<List<Sale>>() {
            @Override
            public void handleResponse(List<Sale> response) {
                InventoryApp.sales = response;
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(context, "טרם נשרמו מכירות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                valid = false;
            }
        });
        return valid;
    }

    public boolean addSaleTable(DataQueryBuilder buyBuilder) {
        Backendless.Data.of(Sale.class).find(buyBuilder, new AsyncCallback<List<Sale>>() {
            @Override
            public void handleResponse(List<Sale> response) {
                InventoryApp.sales.addAll(response);
                valid = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });
        return valid;
    }
}