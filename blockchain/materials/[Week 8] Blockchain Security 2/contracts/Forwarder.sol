// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract Forwarder {
    function forward(address target, bytes calldata data) external {
        (bool ok, ) = target.call(data); // unchecked low-level call
        // no check -> silent failure
    }
}
